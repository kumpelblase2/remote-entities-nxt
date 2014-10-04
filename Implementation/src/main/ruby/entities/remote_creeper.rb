require '../base'
require '../namespace'

module RemoteEntities
	module Entities
		class RemoteCreeperImpl < BaseAttackingEntity
			include RemoteEntities::Entities::RemoteCreeper

			java_signature 'void explode()'
			def explode
				self.explode_with_modifier 1
			end

			java_signature 'void explode(int)'
			def explode_with_modifier(in_modifier)
				self.bukkit_entity.world.create_explosion self.bukkit_entity.location, 3.0 * in_modifier
				self.bukkit_entity.health = 0
			end
		end

		class RemoteCreeperEntity < NMS::EntityCreeper
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