require '../namespace'
require '../service'

module RemoteEntities
	module Services
		class RemoteEntityConversionService
			include ServiceRegistration
			include Java::de.kumpelblase2.remoteentities.services.RemoteEntityConversionService

			def get_remote_entity_from_living(in_living_entity)
				handle = in_living_entity.nms_handle
				handle.is_a?(RemoteEntities::RemoteEntityHandle) ? handle.remote_entity : nil
			end

			register_for Java::de.kumpelblase2.remoteentities.services.RemoteEntityConversionService
		end
	end
end