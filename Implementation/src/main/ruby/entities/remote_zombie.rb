require '../base'
require '../namespace'

module RemoteEntities
    module Entities
        class RemoteZombieImpl < RemoteAttackingEntity
			include RemoteEntities::Entities::RemoteZombie
		end

		class RemoteZombieEntity < NMS::EntityZombie
			extend RemoteEntities::EntityMixins::EntityHandle
			include RemoteEntities::EntityMixins::RemoteMethodDefaults
		end
    end
end
