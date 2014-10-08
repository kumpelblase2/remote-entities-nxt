require '../namespace'
require '../base'
require 'java'

module RemoteEntities
	class DefinitionExecutor

		NMS_PACKAGE = "net.minecraft.server.#{$MC_VERSION}"
		CB_PACKAGE = "org.bukkit.craftbukkit.#{$MC_VERSION}"

		def self.execute(in_definition)
			definitions = in_definition[:definitions]
			execute_entities definitions[:entities]
			execute_nms definitions[:nms]
			execute_cb definitions[:cb]
			execute_custom definitions[:custom]
		end

		def self.execute_entities(in_entities)
			in_entities.each do |type, methods|
				class_name = (type == :default ? 'Entity' : "Entity#{type.to_s.capitalize}")
				apply_for_all "#{NMS_PACKAGE}.#{class_name}", methods
			end
		end

		def self.execute_nms(in_nms)
			in_nms.each do |class_name, methods|
				methods.each do |alias_name, method|
					apply_definition "#{NMS_PACKAGE}.#{class_name}", alias_name, method
				end
			end
		end

		def self.execute_cb(in_cb, curr_package = CB_PACKAGE) #TODO TEST THIS
			in_cb.each do |class_or_package, definition|
				new_package = "#{curr_package}.#{class_or_package}"
				if definition.is_class_def?
					apply_for_all new_package, definition
				else
					execute_cb definition, new_package
				end
			end
		end

		def self.execute_custom(in_custom)
			in_custom.each do |class_name, methods|
				methods.each do |alias_name, method|
					apply_definition class_name, alias_name, method
				end
			end
		end

		def self.apply_definition(in_class_name, in_alias_name, in_def)
			original_method = ''
			args = []
			if in_def.is_a?(String)
				original_method = in_def
			else
				original_method = in_def[:name]
				args = in_def[:params].collect do |type|
					name = type.include? '.' ? to_package(type) : type
					Java::JavaClass.for_name(name)
				end
			end
			Java::JavaClass.for_name(in_class_name).ruby_class.send :java_alias, in_alias_name, original_method, args
		end

		def self.to_package(in_whole_name)
			split = in_whole_name.split /\./
			case split[0]
				when 'nms'
					"#{NMS_PACKAGE}.#{split[-1]}"
				else
					split[-1]
			end
		end

		def self.apply_for_all(in_class_name, in_methods)
			in_methods.each do |alias_name, method|
				apply_definition in_class_name, alias_name, method
			end
		end
	end
end