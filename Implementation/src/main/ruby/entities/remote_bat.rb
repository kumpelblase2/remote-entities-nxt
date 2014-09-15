require '../base'
require '../namespace'
require 'base_attacking_entity'
require '../mixins/custom_overrider'
require '../mixins/sounds'
require '../mixins/entity_handle'
require '../mixins/remote_method_definitions'

module RemoteEntities
	module Entities
		class RemoteBatImpl < BaseAttackingEntity
			include RemoteEntities::Entities::RemoteBat
			include RemoteEntities::EntityMixins::EntitySounds

			def is_hanging
				self.entity.is_hanging?
			end

			def set_hanging(in_hanging)
				self.entity.hanging = in_hanging
			end
		end

		class RemoteBatEntity < NMS::EntityBat
			extend EntityMixins::EntityHandle
			include EntityMixins::RemoteMethodDefaults

			override_for :method => :random_sound
			def remote_bat_random_sound
				self.is_hanging? and self.random.next_int(4) != 0 ? nil : self.remote_entity.get_sound(RemoteEntities::EntitySound::SLEEPING)
			end

			override_for :type => :bat, :method => :tick_movement
			def remote_tick_movement
				self.aV += 1
				self.w
				self.entity_senses.a
				self.target_selector.a
				self.goal_selector.a
				self.world.methodProfiler.b
				self.navigation.update
				self.bk #todo THIS IS WRONG
				self.controller_move.c
				self.controller_look.a
				self.controller_jump.b
			end
		end

		bat_entry = RemoteEntities::RemoteEntityType::Bat
		# todo might need to
		bat_entry.set_remote_class RemoteBatImpl
		bat_entry.set_entity_class RemoteBatEntity
	end
end