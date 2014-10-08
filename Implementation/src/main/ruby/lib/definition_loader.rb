require 'net/http'
require 'IO'
require 'json' # TODO how to do maven with it?
require '../namespace'
require '../base'
require '../definitions/bundled_definitions'

DEF_VERSION = plugin.config.get_string :def_version

=begin

response should look like this:
{
	"latest": {
		"mc_rev": "1_7_R1",
		"version": "1.0.1"
	},
	"available": {
		"1_7_R1": [
			"1.0.1",
			"1.0.0"
		],
		"1_6_R1": [
			"1.1.0",
			"1.0.2",
			"1.0.1",
			"1.0.0"
		]
	}
}

=end


module RemoteEntities
	class DefinitionHash < Hash
		def latest
			versions = self[$MC_VERSION.to_sym]
			(versions.sort { |x ,y| y <=> x })[0]
		end
	end


	class DefinitionLoader
		attr_reader :bundled_definitions
		attr_reader :latest_version
		attr_reader :available_versions
		attr_reader :loaded_definitions

		def initialize
			@bundled_definitions = METHODS
			@loaded_definitions = RemoteEntities::DefinitionHash.new({})
			request_versions if RemoteEntities.plugin.config.get_boolean('update.enabled', true)
		end

		def request_versions
			RemoteEntities.plugin.logger.info 'Loading available versions...'
			url = URI.parse(RemoteEntities::REMOTE_DEFINITION_LIST_URL)
			result = Net::HTTP.get url
			versions = JSON.parse result, :symbolize_names => true
			if versions[:latest] == nil
				RemoteEntities.plugin.logger.severe 'Unable to find working definitions for present RemoteEntities version'
			end
			@latest_version = versions[:latest]
			@available_versions = versions[:available]
			RemoteEntities.plugin.logger.info "Versions loaded. Latest version: #{@latest_version[:version]} compatible with MC-#{@latest_version[:mv_rev]}"
		end

		def load_for(in_rev, in_ops = {})
			version = in_ops[:version_to_load] || self.latest_version[:version]

			if self.is_version_present?(in_rev, version)
				ok, result = self.load_definition in_rev, version
				if ok
					self.add_definition in_rev, version, result
				else
					RemoteEntities.plugin.logger.severe "Unable to read local version definition: #{result}"
				end
			else
				ok, result = self.download_definition in_rev, version
				if ok
					self.add_definition in_rev, version, result
				else
					RemoteEntities.plugin.logger.warning "Unable to download definition: #{result}"
				end
			end
		end

		def add_definition(in_rev, in_version, in_def)
			loaded_definitions[in_rev][in_version] = in_def
		end

		def download_definition(in_rev, in_version)
			content = ''
			begin
				content = Net::HTTP.get URI.parse(RemoteEntities::REMOTE_DEFINITION_VERSIONS + "/#{in_rev}/#{in_version}")
				definition = JSON.parse content
			rescue Exception => e
				return [false, e]
			end
			self.save_definition in_rev, in_version, content
			[true, definition]
		end

		def has_definitions_for_rev?(in_rev)
			File.exists?(in_rev) # TODO Might need relative path to plugin data path?
		end

		def is_version_present?(in_rev, in_version)
			self.has_definitions_for_rev?(in_rev) and File.exists?("#{in_rev}/#{in_version}.json")
		end

		def save_definition(in_rev, in_version, in_def_string)
			Dir.mkdir(in_rev) unless self.has_definitions_for_rev?(in_rev)
			IO.write "#{in_rev}/#{in_version}.json", in_def_string
		end

		def load_definition(in_rev, in_version)
			if self.is_version_present?(in_rev, in_version)
				begin
					result = JSON.parse IO.read("#{in_rev}/#{in_version}.json")
					[true, result]
				rescue Exception => e
					[false, e]
				end
			else
				[false, nil]
			end
		end
	end
end