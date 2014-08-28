require 'java'
require 'namespace'
require 'extensions'

module RemoteEntities
	include_package 'de.kumpelblase2.remoteentities.api'

	def self.plugin
		@@plugin
	end

	module Exceptions
		include_package 'de.kumpelblase2.remoteentities.exceptions'
	end

	module Helpers
		include_package 'de.kumpelblase2.remoteentities.helper'

		module Ruby
			include_package 'de.kumpelblase2.remoteentities.helper.ruby'
		end
	end

	module Events
		include_package 'de.kumpelblase2.remoteentities.api.events'
	end
end

module NMS
	include_package 'net.minecraft.server.v1_7_R1'
end

def bukkit
	org.bukkit.Bukkit
end