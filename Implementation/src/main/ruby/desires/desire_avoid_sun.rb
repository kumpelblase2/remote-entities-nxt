require '../base'

module RemoteEntities
	module Desires
		class DesireAvoidSun < RemoteEntities::Thinking::DesireBase
			def initialize
				super
				self.type = RemoteEntities::Thinking::DesireType::PRIMAL_INSTINCT
			end

			def start_executing
				self.remote_entity.move Bukkit::Location.new(self.remote_entity.bukkit_entity.world, @x, @y, @z)
			end

			def should_execute
				entity = self.remote_entity.entity
				return false unless entity.world.is_day? and entity.is_burning and entity.world.i(NMS::MathHelper.floor(entity.loc_x), entity.bounding_box.b, NMS::MathHelper.floor(entity.loc_z))

				vec = self.shadow_place
				return false unless vec
				@x = vec.c
				@y = vec.d
				@z = vec.e
				NMS::Vec3D.a.release vec
				true
			end

			def can_continue
				not self.remote_entity.entity.navigation.has_path?
			end

			def shadow_place
				entity = self.remote_entity.entity
				random = entity.random
				10.times do
					x = NMS::MathHelper.floor(entity.loc_x + random.next_int(20) - 10)
					y = NMS::MathHelper.floor(entity.bounding_box.b + random.next_int(6) - 3)
					z = NMS::MathHelper.floor(entity.loc_z + random.next_int(20) - 10)

					if entity.is_a?(NMS::EntityCreature)
						return entity.world.vec3D_pool.create x, y, z if (not entity.world.has_sunlight_at?(x, y, z)) and entity.relative_light_power_at(x, y, z) < 0
					else
						return entity.world.vec3D_pool.create x, y, z if (not entity.world.has_sunlight_at?(x, y, z)) and (0.5 - entity.world.light_power_at(x, y, z)) < 0
					end
				end
				nil
			end
		end
	end
end