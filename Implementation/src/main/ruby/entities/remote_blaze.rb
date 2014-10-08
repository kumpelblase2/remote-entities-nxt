require '../base'
require '../namespace'

module RemoteEntities
	module Entities
		class RemoteBlazeImpl < BaseAttackingEntity
			include RemoteEntities::Entities::RemoteBlaze
		end

		class RemoteBlazeEntity < NMS::EntityBlaze
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