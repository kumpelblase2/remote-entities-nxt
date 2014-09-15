require '../base'
require '../namespace'

module RemoteEntities
	module EntityMixins
		module RemoteMethodDefaults
			def self.included(base)
				base.class_eval do
					base.send :include, RemoteUpdate, RemoteCollide, RemoteDeath, RemoteUpdate, RemoteInteract, RemotePush, RemoteNewAI
				end
			end
		end

		module RemoteUpdate
			def self.included(base)
				base.class_eval do
					override_for :type => :default, :method => :update
					def remote_update
						super
						self.remote_entity.mind.tick if self.remote_entity
					end
				end
			end
		end

		module RemoteNewAI
			def self.included(base)
				base.class_eval do
					override_for :type => :default, :method => :new_ai?
					def remote_new_ai
						true
					end
				end
			end
		end

		module RemoteCollide
			def self.included(base)
				base.class_eval do
					override_for :type => :default, :method => :collide
					def remote_collide(in_entity)
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
				end
			end
		end

		module RemoteDeath
			def self.included(base)
				base.class_eval do
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
		end

		module RemotePush
			def self.included(base)
				base.class_eval do
					override_for :type => :default, :method => :push
					def remote_push(in_x, in_y, in_z)
						event = RemoteEntities::Events::RemoteEntityPushEvent.new self.remote_entity, Java::org.bukkit.util.Vector.new(in_x, in_y, in_z)
						event.cancelled = (not self.remote_entity.is_pushable or self.remote_entity.is_stationary)
						bukkit.plugin_manager.call_event event
						unless event.is_cancelled
							vec = event.velocity
							super(vec.x, vec.y, vec.z)
						end
					end
				end
			end
		end

		module RemoteInteract
			def self.included(base)
				base.class_eval do
					override_for :type => :default, :method => :interact
					def remote_interact(in_entity)
						if in_entity.bukkit_entity.is_a?(org.bukkit.entity.Player)
							if self.remote_entity.features.has_feature(RemoteEntities::Features::TradingFeature)
								feature = self.remote_entity.features.get_feature RemoteEntities::Features::TradingFeature
								feature.open_for in_entity.bukkit_entity
								return false
							end

							unless self.remote_entity.mind
								return super
							end

							if self.remote_entity.mind.can_feel
								event = RemoteEntities::Events::RemoteEntityInteractEvent.new self.remote_entity, in_entity.bukkit_entity
								bukkit.plugin_manager.call_event event
								return false if event.is_cancelled
								self.remote_entity.mind.get_behavior(RemoteEntities::Thinking::InteractBehavior).on_interact(in_entity.bukkit_entity) if self.remote_entity.mind.has_behavior(RemoteEntities::Thinking::InteractBehavior)
							end
						else
							super
						end
					end
				end
			end
		end
	end
end