require 'namespace'
require 'base'

module RemoteEntities
	module Services
		module ServiceRegistration
			class << self
				def register_for(in_java_class)
					bukkit.services_manager.register in_java_class, self, RemoteEntities.plugin, Java::org.bukkit.plugin.ServicePriority::Normal
				end
			end
		end
	end
end