require 'base'

module RemoteEntities
	module Entities
		def self.setup(in_type, &block)
			su = SetupDefinition.new in_type
			su.evaluate &block
		end

		class SetupDefinition
			def initialize(in_type)
				@type = in_type
			end

			def evaluate(&block)
				instance_eval &block
			end

			def with_sounds
				# TODO
			end

			def with_classes(in_remote_class, in_entity_class)
				type = RemoteEntities::RemoteEntityType.value_of in_type
				type.set_remote_class in_remote_class
				type.set_entity_class in_entity_class
			end
		end
	end
end