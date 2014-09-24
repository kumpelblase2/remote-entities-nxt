require '../base'

module RemoteEntities
	module Desires
		class DesireAcceptFlower < RemoteEntities::Thinking::DesireBase
			def initialize
				super
				self.type = RemoteEntities::Thinking::DesireType::FULL_CONCENTRATION
			end

			def should_execute
				nms_entity = self.remote_entity.entity
				if nms_entity.is_a? NMS::EntityAgeable && nms_entity.age >= 0
					return false
				elsif not entity.world.is_day?
					return false
				else
					golems = nms_entity.world.get_typed_entities_in NMS::EntityIronGolem, nms_entity.bounding_box.grow(6, 2, 6)
					unless golems.is_empty
						it = golems.iterator
						while it.has_next
							golem = it.next
							if golem.bZ() > 0 # TODO what's bZ?
								@nearest_golem = golem
								break
							end
						end
					else
						@nearest_golem = nil
					end

					@nearest_golem != nil
				end
			end

			def can_continue
				@nearest_golem && @nearest_golem.q > 0 # TODO what's q?
			end

			def start_executing
				@take_flower_tick = self.remote_entity.entity.random.next_int(320)
				@take_flower = false
				@nearest_golem.navigation.stop # TODO is this true?
			end

			def stop_executing
				@nearest_golem = nil
				self.remote_entity.entity.navigation.stop
			end

			def update
				entity = self.remote_entity.entity
				entity.controller_look.a(@nearest_golem, 30, 30) #TODO a should be proxied
				if @nearest_golem.bZ() == @take_flower_tick
					entity.navigation.a(@nearest_golem, self.remote_entity.speed)
					@take_flower = true
				end

				if @take_flower && entity.distance_to_entity(@nearest_golem) < 4
					@nearest_golem.flower_in_hand = false
					entity.navigation.stop
				end

				true
			end
		end
	end
end