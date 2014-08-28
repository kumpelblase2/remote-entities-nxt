require '../namespace'
require 'base_entity'

module RemoteEntities
	module Entities
		class BaseAttackingEntity < BaseEntity
			include Java::de.kumpelblase2.remoteentities.api.Fightable

			def attack(in_target)
				if self.is_spawned
					# todo set goal target
				end
			end

			def lose_target
				if self.is_spawned
					# todo lose target
				end
			end

			def get_target
				if self.is_spawned
					# todo get target
				end
			end
		end
	end
end

