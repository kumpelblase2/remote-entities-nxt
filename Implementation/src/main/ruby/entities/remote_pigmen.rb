require '../base'
require '../namespace'

module RemoteEntities
	module Entities
		class RemotePigmenImpl < BaseAttackingEntity
			include RemoteEntities::Entities::RemotePigmen
		end

		class RemotePigmenEntity < NMS::EntityPigZombie
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