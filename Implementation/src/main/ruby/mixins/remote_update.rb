require '../base'
require '../namespace'

module RemoteEntities
	module EntityMixins
		module RemoteUpdate
			class << self
				override_for :type => :default, :method => :update
				def remote_update
					super
					if self.remote_entity
						self.remote_entity.mind.tick
					end
				end
			end
		end

		module RemoteCollide
			class << self
				override_for :type => :default, :method => :collide
				def remote_collide(in_entity)
					if self.remote_entity
						if not @last_bounced_id or @last_bounced_id != in_entity.entity_id or (Time.now - @last_bounced_time) > 1
							event = RemoteEntities::Events::RemoteEntityTouchEvent.new self, in_entity
							bukkit.plugin_manager.call_event event
							if event.is_cancelled
								return false
							end

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
			end
		end

		module RemoteDeath
			class << self
				override_for :type => :default, :method => :death
				def remote_death(in_damage_source)
					if self.remote_entity.mind.has_behavior(RemoteEntities::Thinking::DeathBehavior)
						self.remote_entity.mind.get_behavior(RemoteEntities::Thinking::DeathBehavior).on_death
					end

					self.remote_entity.mind.clear_movement_desires
					self.remote_entity.mind.clear_targeting_desires
					super
				end
			end
		end

		module RemotePush
			class << self
				override_for :type => :default, :method => :push
				def remote_push(in_x, in_y, in_z)
					event = RemoteEntities::Events::RemoteEntityPushEvent.new self, Java::org.bukkit.util.Vector.new(in_x, in_y, in_z)
					event.cancelled = (not self.remote_entity.is_pushable or self.remote_entity.is_stationary)
					bukkit.plugin_manager.call_event event
					unless event.is_cancelled
						vec = event.velocity
						super(vec.x, vec.y, vec.z)
					end
				end
			end
		end

		module RemoteInteract
			class << self
				override_for :type => :default, :method => :interact
				def remote_interact(in_entity)
					if in_entity.bukkit_entity.is_a?(org.bukkit.entity.Player)

					else
						super
					end
				end
			end
		end
	end
end