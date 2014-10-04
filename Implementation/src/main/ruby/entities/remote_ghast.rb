require '../base'
require '../namespace'

module RemoteEntities
	module Entities
		class RemoteGhastImpl < BaseAttackingEntity
			include RemoteEntities::Entities::RemoteGhast
		end

		class RemoteGhastEntity < NMS::EntityGhast
			extend RemoteEntities::EntityMixins::EntityHandle
			include RemoteEntities::EntityMixins::RemoteMethodDefaults

			# TODO do we need the override? or can we maybe just use attr_accessor ?
			def target=(in_target)
				@target = in_target
			end

			def target
				@target
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