require '../base'
require '../namespace'

module RemoteEntities
	module Entities
		class RemoteVillagerImpl < BaseEntity
			include RemoteEntities::Entities::RemoteVillager
		end

		class RemoteVillagerEntity < NMS::EntityVillager
			extend RemoteEntities::EntityMixins::EntityHandle
			include RemoteEntities::EntityMixins::RemoteMethodDefaults

			java_signature "#{NMS_PACKAGE}.EntityAgeable createChild(#{NMS_PACKAGE}.EntityAgeable)"
			def create_child(in_partner)
				self.b in_partner
			end

			java_signature "void a_(#{NMS_PACKAGE}.ItemStack)"
			def a__(in_item)
				if (not self.world.is_static) && self.a_ > -self.q + 20
					self.a_ = -self.q()
					if in_item
						self.make_sound self.remote_entity.get_sound(RemoteEntities::EntitySound::YES), self.bf, self.bg
					else
						self.make_sound self.remote_entity.get_sound(RemoteEntities::EntitySound::NO), self.bf, self.bg
					end
				end
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
