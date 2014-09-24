require '../base'
require '../namespace'

module RemoteEntities
	module Entities
		class RemoteWitchImpl < RemoteAttackingEntity
			include RemoteEntities::Entities::RemoteWitch
		end

		class RemoteWitchEntity < NMS::EntityWitch
			extend RemoteEntities::EntityMixins::EntityHandle
			include RemoteEntities::EntityMixins::RemoteMethodDefaults

			def self.default_movement_desires
				[
						# TODO
				]
			end

			def self.default_target_desires
				[
						# TODO
				]
			end
		end
	end
end
