require '../base'
require '../namespace'

module RemoteEntities
	module Entities
		class RemoteSnowmanImpl < BaseAttackingEntity
			include RemoteEntities::Entities::RemoteSnowman
		end

		class RemoteSnowmanEntity < NMS::EntitySnowman
			extend RemoteEntities::EntityMixins::EntityHandle
			include RemoteEntities::EntityMixins::RemoteMethodDefaults

			java_signature "void a(#{NMS_PACKAGE}.EntityLiving, float)" # TODO should be override_for
			def a2(in_entity, in_float)
				snowball = NMS::EntitySnowball.new(self.world, self)
				d0 = in_entity.loc_x - self.loc_x
				d1 = in_entity.loc_y + in_entity.head_height - 1.100000023841858 - snowball.loc_y
				d2 = in_entity.loc_z - self.loc_z
				f1 = NMS::MathHelper.sqrt(d0 * d0 + d2 * d2) * 0.2
				snowball.shoot d0, d1 + f1, d2, 1.6, 12
				self.make_sound self.remote_entity.get_sound(RemoteEntities::EntitySound::ATTACK), 1.0, 1.0 / (self.random.next_float * 0.4 + 0.8)
				self.world.add_entity snowball
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