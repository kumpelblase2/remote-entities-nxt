require '../base'
require '../namespace'

module RemoteEntities
	module Entities
		class RemoteWitherImpl < RemoteAttackingEntity
			include RemoteEntities::Entities::RemoteWither
		end

		class RemoteWitherEntity < NMS::EntityWither
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
