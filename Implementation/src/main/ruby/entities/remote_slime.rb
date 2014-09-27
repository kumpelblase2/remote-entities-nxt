require '../base'
require '../namespace'

module RemoteEntities
	module Entities
		class RemoteSlimeImpl < BaseAttackingEntity
			include RemoteEntities::Entities::RemoteSlime
		end

		class RemoteSlimeEntity < NMS::EntitySlime
			extend RemoteEntities::EntityMixins::EntityHandle
			include RemoteEntities::EntityMixins::RemoteMethodDefaults

			def on_create
				@jump_delay = self.random.next_int(20) + 10
			end

			override_for :method => :update
			def slime_update
				self.w
				self.a @target, 10.0, 20.0 if @target

				if self.on_ground
					@jump_delay -= 1
				end

				if self.on_ground and @jump_delay <= 0
					@jump_delay = self.bP
					@jump_delay /= 3 if @target

					self.bd = true
					self.make_sound self.bT, self.bf, ((self.random.next_float - self.random.next_float) * 0.2 + 1.0) * 0.8 if self.bW
					self.be = 1.0 - self.random.next_float * 2.0
					self.bf = self.size
				else
					self.bd = false
					self.be = self.bf = 0.0 if self.on_ground
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