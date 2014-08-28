require '../namespace'
require '../service'

module RemoteEntities
	module Services
		class NMSEntityService
			include ServiceRegistration
			include Java::de.kumpelblase2.remoteentities.services.NMSEntityService

			def check_affected(in_entity)
				handle = in_entity.nms_handle
				(not handle.is_a?(NMS::EntityAnimal)) or handle.cc
			end

			def reset_affection(in_entity)
				handle = in_entity.nms_handle
				if handle.is_a?(NMS::EntityAnimal)
					handle.cd
				end
			end

			register_for Java::de.kumpelblase2.remoteentities.services.NMSEntityService
		end
	end
end
