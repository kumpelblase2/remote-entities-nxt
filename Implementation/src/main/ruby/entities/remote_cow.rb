require '../base'
require '../namespace'

module RemoteEntities
	module Entities
		class RemoteCowImpl < BaseEntity
			include RemoteEntities::Entities::RemoteCow
		end

		class RemoteCowEntity < NMS::EntityCow
			extend RemoteEntities::EntityMixins::EntityHandle
			include RemoteEntities::EntityMixins::RemoteMethodDefaults

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