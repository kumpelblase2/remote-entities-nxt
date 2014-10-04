require '../base'
require '../namespace'

module RemoteEntities
	module Entities
		class RemoteEndermanImpl < BaseAttackingEntity
			include RemoteEntities::Entities::RemoteEnderman
		end

		class RemoteSlimeEntity < NMS::EntityEnderman
			extend RemoteEntities::EntityMixins::EntityHandle
			include RemoteEntities::EntityMixins::RemoteMethodDefaults

			override_for :type => :enderman, :method => :k #todo
			def enderman_teleport(d0, d1, d2)
				d3 = self.loc_x
				d4 = self.loc_y
				d5 = self.loc_z

				self.loc_x = d0
				self.loc_y = d1
				self.loc_z = d2
				flag = false
				i = NMS::MathHelper.floor self.loc_x
				j = NMS::MathHelper.floor self.loc_y
				k = NMS::MathHelper.floor self.loc_z

				if self.world.is_loaded(i, j, k)
					flag1 = false
					while (not flag1) and j > 0
						block = self.world.get_type i, j - 1, k
						if block.material != NMS::Material::AIR and block.material.is_solid
							flag1 = true
						else
							self.loc_y -= 1
							j -= 1
						end
					end

					if flag1
						self.set_position self.loc_x, self.loc_y, self.loc_z
						if self.world.get_cubes(self, self.bounding_box).is_empty and not self.world.contains_liquid(self.bounding_box)
							flag = true
						end
					end
				end

				unless flag
					self.set_position d3, d4, d5
					false
				else
					short1 = 128
					short1.times do |l|
						d6 = l / (short1 - 1.0)
						f = (self.random.next_float() - 0.5) * 0.2
						f1 = (self.random.next_float() - 0.5) * 0.2
						f2 = (self.random.next_float() - 0.5) * 0.2
						d7 = d3 + (self.loc_x - d3) * d6 + (self.random.next_double() - 0.5) * self.width * 2.0
						d8 = d4 + (self.loc_x - d4) * d6 + self.random.next_double() * self.length
						d9 = d5 + (self.loc_z - d5) * d6 + (self.random.next_double() - 0.5) * self.width * 2.0

						self.world.add_particle 'portal', d7, d8, d9, f, f1, f2
					end

					self.world.make_sound d3, d4, d5, self.remote_entity.get_sound(RemoteEntities::EntitySound::TELEPORT), 1.0, 1.0
					self.make_sound self.remote_entity.get_sound(RemoteEntities::EntitySound::TELEPORT), 1.0, 1.0
					true
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