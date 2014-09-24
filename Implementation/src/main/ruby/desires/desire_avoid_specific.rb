require '../base'

module RemoteEntities
	module Desires
		class DesireAvoidSpecific < RemoteEntities::Thinking::DesireBase
			def initialize(in_min_distance, in_close_speed, in_far_speed, in_to_avoid, in_ignore_out_of_sight = true)
				super()
				if NMS::Entity.java_class.is_assignable_from(in_to_avoid)
					@to_avoid = in_to_avoid
				else
					@to_avoid = RemoteEntities::Helpers::NMSClassMap.get_nmsclass in_to_avoid
				end

				@min_distance = in_min_distance
				@far_speed = in_far_speed
				@close_speed = in_close_speed
				@ignore_out_of_sight = in_ignore_out_of_sight
				self.type = RemoteEntities::Thinking::DesireType::PRIMAL_INSTINCT
			end

			def on_add(in_entity)
				super in_entity
				@selector = nil # TODO
			end

			def start_executing
				# TODO move path
			end

			def stop_executing
				@closest_entity = nil
			end

			def update
				unless @closest_entity.is_alive
					return false
				end

				if self.remote_entity.entity.distance_to_entity(@closest_entity) > 49
					self.remote_entity.entity.navigation.speed = @far_speed
				else
					self.remote_entity.entity.navigation.speed = @close_speed
				end

				true
			end

			def should_execute
				entity = self.remote_entity.entity
				if @to_avoid == NMS::EntityHuman
					if entity.is_a?(NMS::EntityTameableAnime) && entity.is_tamed
						return false
					end

					@closest_entity = entity.world.find_nearby_player entity, @min_distance
					return false unless @closest_entity
				else
					close_entities = entity.world.get_typed_entities_in_with_selector @to_avoid, entity.bounding_box.grow(@min_distance, 3, @min_distance), @selector
					return false if close_entities.is_empty
					@closest_entity = close_entities.get 0
				end

				if (not @ignore_out_of_sight) && (not entity.entity_senses.can_see(@closest_entity))
					return false
				end

				vec = RemoteEntities::Helpers::World::RandomPositionGenerator.b entity, 16, 7, NMS::Vec3D.a.create(@closest_entity.loc_x, @closest_entity.loc_y, @closest_entity.loc_z)
				return false if vec == nil

				if @closest_entity.distance_to_location(vec.c, vec.d, vec.e) < @closest_entity.distance_to_entity(entity)
					NMS::Vec3D.a.release(vec)
					false
				else
					@path = entity.navigation.create_path vec.c, vec.d, vec.e
					NMS::Vec3D.a.release vec
					@path != nil
				end
			end

			def can_continue
				not self.remote_entity.entity.navigation.has_path?
			end
		end
	end
end