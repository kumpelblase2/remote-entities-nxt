require '../namespace'
require '../base'
require '../extensions'
require '../mixins/inventory_holder'
require '../mixins/inventory_copier'

module RemoteEntities
	module Entities
		class BaseEntity < Java::de.kumpelblase2.remoteentities.api.BaseRemoteEntity
			include RemoteEntities::EntityMixins::InventoryHolder
			include RemoteEntities::EntityMixins::InventoryCopier

			attr_reader :entity
			attr_reader :m_speed
			attr_accessor :unloaded_location

			def initialize(in_id, in_type, in_manager)
				super
				@entity = nil
				self.pushable = true
				self.stationary = false
				self.speed = -1
			end

			java_signature 'boolean move(org.bukkit.Location, float)'
			def move_by_location(in_location, in_speed)
				unless in_speed
					in_speed = self.speed
				end

				if self.is_stationary or not self.is_spawned or self.entity.on_leash?
					if self.entity.navigation.a(in_location.x, in_location.y, in_location.z, in_speed)
						true
					else
						path = self.entity.world.a(self.entity, NMS::MathHelper.floor(in_location.x), in_location.y, NMS::MathHelper.floor(in_location.z), self.pathfinding_range, true, false, false, true)
						self.move_with_path path, in_speed
					end
				end
			end

			java_signature 'boolean move(org.bukkit.entity.LivingEntity, float)'
			def move_by_entity(in_entity, in_speed)
				unless in_speed
					in_speed = self.speed
				end

				if self.is_stationary or not self.is_spawned or self.entity.on_leash? and not self.entity.eql?(in_entity.nms_handle)
					unless self.entity.navigation.a(in_entity.nms_handle, in_speed)
						path = self.entity.world.find_path(self.entity, in_entity.nms_handle, self.pathfinding_range, true, false, false, true)
						self.move_with_path path, in_speed
					end
				end
			end

			def move_with_path(in_path, in_speed)
				if self.entity and in_path and not self.is_stationary
					if self.entity.is_a?(NMS::EntityCreature)
						self.entity.path_entity = in_path
					end

					self.entity.navigation.a in_path, in_speed
				else
					false
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

			java_signature 'void lootAt(org.bukkit.Location)'
			def look_at_location(in_location)
				if self.is_spawned
					self.entity.controller_look.a in_location.x, in_location.y, in_location.z, 10, self.entity.max_head_rotation
				end
			end

			java_signature 'void lookAt(org.bukkit.entity.Entity)'
			def look_at_entity(in_entity)
				if self.is_spawned
					self.entity.controller_look.a(in_entity.nms_handle, 10, self.entity.max_head_rotation)
				end
			end

			def stop_moving
				if self.is_spawned
					self.entity.navigation.h
				end
			end

			java_signature 'void teleport(org.bukkit.Location)'
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

			def native_entity_name
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

			java_signature 'double getSpeed()'
			def speed
				if self.is_spawned
					self.entity.get_attribute_instance(NMS::GenericAttributes.d).value
				else
					self.m_speed != -1 ? self.m_speed : NMS::GenericAttributes.d.b
				end
			end
		end
	end
end