require '../base'
require '../namespace'

module RemoteEntities
	module Entities
		class RemoteHorseImpl < BaseEntity
			include RemoteEntities::Entities::RemoteHorse
		end

		class RemoteHorseEntity < NMS::EntityHorse
			extend RemoteEntities::EntityMixins::EntityHandle
			include RemoteEntities::EntityMixins::RemoteMethodDefaults

			override_for :type => :horse, :method => :b #todo
			def land_sound(in_f)
				if in_f > 1
					self.make_sound self.remote_entity.get_sound(RemoteEntities::EntitySound::LAND), 0.4, 1.0
				end

				i = NMS::MathHelper.f(in_f * 0.5 - 3.0)
				if i > 0
					self.damage_entity NMS::DamageSource.FALL, i
					if self.passenger
						self.passenger.damage_entity NMS::DamageSource.FALL, i
					end

					j = self.world.get_type NMS::MathHelper.floor(self.loc_x), MathHelper.floor(self.loc_y - 0.2 - self.last_yaw), NMS::MathHelper.floor(self.loc_z)
					unless j.material == NMS::Material::AIR
						sound = j.stepsound
						self.world.make_sound self, sound.step_sound, sound.volume1 * 0.5, sound.volume2 * 0.75
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