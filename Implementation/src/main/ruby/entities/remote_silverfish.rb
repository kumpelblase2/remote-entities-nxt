require '../base'
require '../namespace'

module RemoteEntities
	module Entities
		class RemoteSilverfishImpl < BaseAttackingEntity
			include RemoteEntities::Entities::RemoteSilverfish
		end

		class RemoteSilverfishEntity < NMS::EntitySilverfish
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