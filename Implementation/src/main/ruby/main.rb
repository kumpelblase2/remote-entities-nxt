require 'java'
require 'namespace'
require 'base'
require 'service'

module RemoteEntities
	class RemoteEntityManagerFactory
		require 'ruby_entity_manager'

		include Java::de.kumpelblase2.remoteentities.EntityManagerFactory

		def create_manager
			RemoteEntities::RubyEntityManager.new
		end
	end
end