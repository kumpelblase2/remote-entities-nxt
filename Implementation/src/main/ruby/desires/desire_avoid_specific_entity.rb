require '../base'

module RemoteEntities
	module Desires
		class DesireAvoidSpecificEntity < RemoteEntities::Thinking::DesireBase
			def initialize(in_min_distance, in_close_speed, in_far_speed, in_to_avoid)
				super()
				@min_distance = in_min_distance
				@close_speed = in_close_speed
				@far_speed = in_far_speed
				@to_avoid = in_to_avoid.nms_handle
				self.type = RemoteEntities::Thinking::DesireType::PRIMAL_INSTINCT
			end

			def on_add(in_entity)
				super
				@selector = nil # TODO
			end

			def should_execute
				return false if not (@to_avoid and @to_avoid.is_alive) or (not self.remote_entity.entity.entity_senses.can_see(@to_avoid))

				vec = RemoteEntities::Helpers::World::RandomPositionGenerator.b self.remote_entity.entity, 16, 7, NMS::Vec3D.a.create(@to_avoid.loc_x, @to_avoid.loc_y, @to_avoid.loc_z)
				return false unless vec

				if @to_avoid.distance_to_location(vec.c, vec.d, vec.e) < @to_avoid.distance_to_entity(self.remote_entity.entity)
					NMS::Vec3D.a.release vec
					false
				else
					@path = self.remote_entity.entity.navigation.create_path vec.c, vec.d, vec.e
					ret = @path and @path.b vec
					NMS::Vec3D.a.release vec
					ret
				end
			end

			def start_executing
				# todo move path
			end

			def stop_executing
				self.remote_entity.entity.navigation.stop
			end

			def can_continue
				not self.remote_entity.entity.navigation.has_path? and @to_avoid.is_alive
			end

			def update
				return false unless @to_avoid and @to_avoid.is_alive

				self.remote_entity.entity.navigation.speed = (self.remote_entity.entity.distance_to_entity(@to_avoid) > 49 ? @far_speed : @close_speed)
				true
			end

			def is_finished
				not (@to_avoid and @to_avoid.is_alive)
			end
		end
	end
end