require '../namespace'

module RemoteEntities
	module Entities
		class BaseEntity < Java::de.kumpelblase2.remoteentities.api.BaseRemoteEntity
			include Java::de.kumpelblase2.remoteentities.api.RemoteEntity

			attr_reader :entity
			attr_accessor :unloaded_location

			def initialize(in_id, in_type, in_manager)
				super
				@entity = nil
				self.pushable = true
				self.stationary = false
				self.speed = -1
			end

			def move(in_location, in_speed)
				unless in_speed
					in_speed = self.speed
				end

				if self.stationary or not self.is_spawned # or NMSUtil.isOnLeash
					#if(!NMSUtil.getNavigation(this.m_entity).a(inLocation.getX(), inLocation.getY(), inLocation.getZ(), inSpeed))
					#{
					#		PathEntity path = this.m_entity.world.a(this.getHandle(), MathHelper.floor(inLocation.getX()), (int) inLocation.getY(), MathHelper.floor(inLocation.getZ()), (float)this.getPathfindingRange(), true, false, false, true);
					#	return this.moveWithPath(path, inSpeed);
					#}
					true
				end
			end

			def set_yaw(in_yaw, in_move)
				if self.is_spawned
					new_loc = self.bukkit_entity.location
					new_loc.yaw = in_yaw
					if in_move
						self.move new_loc
					else
						if self.is_stationary
							self.mind.fix_yaw_at in_yaw
						end

						self.entity.yaw = in_yaw
						self.entity.aO = in_yaw
					end
				end
			end

			def set_pitch(in_pitch)
				if self.is_spawned
					if self.is_stationary
						self.mind.fix_pitch_at in_pitch
					end

					self.entity.pitch = in_pitch
				end
			end

			def set_head_yaw(in_head_yaw)
				if self.is_spawned
					if self.is_stationary
						self.mind.fix_head_yaw_at in_head_yaw
					end

					self.entity.aP = in_head_yaw
					self.entity.aQ = in_head_yaw
					unless entity.is_a?(NMS::EntityHuman)
						self.entity.aN = in_head_yaw
					end
				end
			end

			def look_at(in_location)
				if self.is_spawned
					# todo controller look
				end
			end

			def stop_moving
				if self.is_spawned
					# todo stop nav
				end
			end

			def teleport(in_location)
				self.bukkit_entity.teleport in_location
			end

			def despawn(in_reason)
				event = Events::RemoteEntityDespawnEvent.new self, in_reason
				bukkit.plugin_manager.call_event event
				if event.is_cancelled and in_reason != RemoteEntities::DespawnReason::PLUGIN_DISABLE
					return false
				end

				if in_reason != RemoteEntities::DespawnReason::CHUNK_UNLOAD and in_reason != RemoteEntities::DespawnReason::NAME_CHANGE
					self.mind.behaviours.each { |behaviour|
						behaviour.on_remove
					}

					self.mind.clear_behaviours
				else
					self.unloaded_location = self.is_spawned ? self.bukkit_entity.location : nil
				end

				if self.is_spawned
					self.bukkit_entity.remove
				end

				@entity = nil
				true
			end

			def is_spawned
				not self.entity.nil?
			end

			def add_speed_modifier(in_amount, in_additive)
				modifier = RemoteEntities::Helpers::RemoteSpeedModifier.new(in_amount, in_additive)
				if self.is_spawned
					instance = self.entity.get_attribute_instance(NMS::GenericAttributes.d)
					instance.b(modifier)
					instance.a(modifier)
				else
					@speed_modifier = modifier
				end
			end

			def remove_speed_modifier
				if self.is_spawned
					self.entity.get_attribute_instance(NMS::GenericAttributes.d).b(RemoteEntities::Helpers::RemoteSpeedModifier.new(0, false))
				else
					@speed_modifier = nil
				end
			end

			def set_pathfinding_range(in_range)
				self.entity.get_attribute_instance(NMS::GenericAttributes.d).set_value in_range
			end

			def get_pathfinding_range
				self.entity.get_attibute_instance(NMS::GenericAttributes.d).get_value
			end

			def get_native_entity_name
				'CUSTOM'
			end

			def set_name(in_name)
				if self.is_spawned
					if in_name.nil?
						self.bukkit_entity.custom_name_visible = false
						self.bukkit_entity.custom_name = null
					else
						self.bukkit_entity.custom_name_visible = true
						self.bukkit_entity.custom_name = in_name
					end
				else
					# todo
				end
			end

			def get_name
				if self.is_spawned
					self.bukkit_entity.custom_name
				else
					# todo
				end
			end
		end
	end
end