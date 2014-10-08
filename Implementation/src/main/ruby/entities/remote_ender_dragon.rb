require '../base'
require '../namespace'

module RemoteEntities
	module Entities
		class RemoteEnderDragonImpl < BaseAttackingEntity
			include RemoteEntities::Entities::RemoteEnderDragon

			@should_destroy_blocks = false
			@fly_normally = false

			java_signature 'boolean move(org.bukkit.Location, double)'
			def move(in_location, in_speed)
				self.entity.target_location = in_location
				true
			end

			java_signature 'boolean shouldDestroyBlocks()'
			def should_destroy_blocks
				@should_destroy_blocks
			end

			java_signature 'void shouldDestroyBlocks(boolean)'
			def should_destroy_blocks=(in_state)
				@should_destroy_blocks = in_state
			end

			java_signature 'boolean shouldFlyNormally()'
			def should_fly_normally
				@fly_normally
			end

			java_signature 'void shouldFlyNormally(boolean)'
			def should_fly_normally=(in_state)
				@fly_normally = in_state
			end
		end

		class RemoteEnderDragonEntity < NMS::EntityEnderDragon
			extend RemoteEntities::EntityMixins::EntityHandle
			include RemoteEntities::EntityMixins::RemoteMethodDefaults

			@target_location = nil

			override_for :method => :tick
			def enderdragon_tick
				super
				self.remote_entity.mind.tick

				unless @target_location
					self.h = @target_location.x
					self.i = @target_location.y
					self.j = @target_location.z
				end
			end

			override_for :method => :update_free_movement
			def enderdragon_free_movement
				if self.remote_entity.is_stationary
					if self.health <= 0
						f = (self.random.next_float() - 0.5) * 8.0
						d05 = (self.random.next_float() - 0.5) * 4.0
						f1 = (self.random.next_float() - 0.5) * 8.0
						self.world.add_particle 'largeexplode', self.loc_x + f, self.loc_y + 2.0 + d05, self.loc_z + f1, 0.0, 0.0, 0.0
					end
					return
				elsif self.remote_entity.mind.has_behavior(RemoteEntities::Thinking::RideBehavior)
					mot = [0, 0, 0].to_java :float
					self.remote_entity.mind.get_behavior(RemoteEntities::Thinking::RideBehavior).ride mot
					self.e(mot[0], mot[1])
					self.mot_y = mot[2]
					self.remote_entity.yaw = (self.yaw < 0 ? self.yaw + 180 : self.yaw - 180)
					return
				end

				if self.remote_entity.should_fly_normally
					super
				else
					unless @target_location
						if self.bukkit_entity.location.distance_squared(@target_location) <= 4
							@target_location = nil
							return
						end

						self.h = @target_location.x
						self.i = @target_location.y
						self.j = @target_location.z
						super
					end
				end
			end

			override_for :method => :collide
			def enderdragon_collide(in_entity)
				unless in_entity.passenger == self or in_entity.vehicle == self
					d0 = in_entity.loc_x - self.loc_x
					d1 = in_entity.loc_z - self.loc_z
					d2 = NMS::MathHelper.a d0, d1

					if d2 >= 0.009999999776482582
						d2 = NMS::MathHelper.sqrt d2
						d0 = d0 / d2
						d1 = d1 / d2
						d3 = 1.0 / d2

						if d3 > 1.0
							d3 = 1.0
						end

						d0 = d0 * d3
						d1 = d1 * d3
						d0 = d0 * 0.05000000074505806
						d1 = d1 * 0.05000000074505806
						d0 = d0 * (1.0 - self.Z)
						d1 = d1 * (1.0 - self.Z)
						self.g -d0, 0.0, -d1
						in_entity.g d0, 0.0, d1
					end
				end

				if self.remote_entity
					if not @last_bounced_id or @last_bounced_id != in_entity.entity_id or (Time.now - @last_bounced_time) > 1
						event = RemoteEntities::Events::RemoteEntityTouchEvent.new self.remote_entity, in_entity
						bukkit.plugin_manager.call_event event
						return false if event.is_cancelled

						if in_entity.is_a?(NMS::EntityHuman) && self.remote_entity.mind.can_feel && self.remote_entity.mind.has_behavior(RemoteEntities::Thinking::TouchBehavior)
							if in_entity.bukkit_entity.location.distance_squared(self.bukkit_entity.location) <= 1
								self.remote_entity.mind.get_behavior(RemoteEntities::Thinking::TouchBehavior).on_touch(in_entity.bukkit_entity)
							end
						end
					end

					@last_bounced_id = in_entity.entity_id
					@last_bounced_time = Time.now
					super
				end
			end

			def target_location=(in_location)
				@target_location = in_location
			end

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