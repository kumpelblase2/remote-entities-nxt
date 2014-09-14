require 'java'
require 'base'
require 'namespace'
require 'definition_loader'
require 'definition_executor'

module RemoteEntities

	DEFINITION_LOADER = DefinitionLoader.new

	class PolicyPWN
		@@current_defs = nil

		def self.current_definitions
			@@current_defs
		end

		def self.get_rekt
			version_to_use = RemoteEntities.plugin.config.get_string('versions.use', 'latest').to_sym
			@@current_defs = (version_to_use == :latest ? DEFINITION_LOADER.loaded_definitions.latest : DEFINITION_LOADER.loaded_definitions[$MC_VERSION.to_sym][version_to_use])
			RemoteEntities::DefinitionExecutor.execute @@current_defs
		end
	end
end