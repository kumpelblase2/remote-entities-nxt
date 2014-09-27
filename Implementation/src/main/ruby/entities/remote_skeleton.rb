require '../base'
require '../namespace'

module RemoteEntities
	module Entities
		class RemoteSkeletonImpl < BaseAttackingEntity
			include RemoteEntities::Entities::RemoteSkeleton
		end

		class RemoteSkeletonEntity < NMS::EntitySkeleton
			extend RemoteEntities::EntityMixins::EntityHandle
			include RemoteEntities::EntityMixins::RemoteMethodDefaults

			java_signature "void a (#{NMS_PACKAGE}.EntityLiving, float)" # TODO should be override_for
			def a2(in_entity, in_float)
				arrow = NMS::EntityArrow.new self.world, self, in_entity, 1.6, 14 - self.world.difficulty.int_value * 4
				i = NMS::EnchantmentManager.get_enchantment_level NMS::Enchantment.ARROW_DAMAGE.id, self.be
				j = NMS::EnchantmentManager.get_enchantment_level NMS::Enchantment.ARROW_KNOCKBACK.id, self.be

				arrow.b((in_float * 2) + self.random.next_gaussian * 0.25 + (self.world.diffculty.int_value * 0.11))
				arrow.b(arrow.e + i * 0.5 + 0.5) if i > 0
				arrow.a(j) if j > 0
				arrow.on_fire = 100 if NMS::EnchantmentManager.get_enchantment_level(NMS::Enchantment.ARROW_FIRE.id, self.be) > 0 or self.skeleton_type == 1
				self.make_sound self.remote_entity.get_sound(RemoteEntities::EntitySound::ATTACK), 1.0, 1.0 / self.random.next_float * 0.4 + 0.8
				self.world.add_entity arrow
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