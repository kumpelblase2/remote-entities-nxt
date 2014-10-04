require '../base'
require '../namespace'

module RemoteEntities
	module Entities
		class RemoteIronGolemImpl < BaseAttackingEntity
			include RemoteEntities::Entities::RemoteIronGolem
		end

		class RemoteIronGolemEntity < NMS::EntityIronGolem
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