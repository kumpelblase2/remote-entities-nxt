require '../namespace'
require 'base_entity'

module RemoteEntities
	module Entities
		class BaseAttackingEntity < Entities::BaseEntity
			include Java::de.kumpelblase2.remoteentities.api.Fightable

			def attack(in_target)
				if self.is_spawned
					self.entity.goal_target = in_target.nms_handle
				end
			end

			def lose_target
				if self.is_spawned
					self.entity.goal_target = nil
				end
			end

			def get_target
				if self.is_spawned
					self.entity.goal_target
				end
			end
		end
	end
end

