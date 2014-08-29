require 'java'

class Object
	def nms_handle;
	end

	def setup_sounds;
	end
end

# ALL ENTITIES WITH ONE LINE GG
Java::JavaClass.for_name("org.bukkit.craftbukkit.#{$MINECRAFT_VERSION}.entity.CraftEntity").ruby_class.send :alias_method, :nms_handle, :get_handle

# WORLD
Java::JavaClass.for_name("org.bukkit.craftbukkit.#{$MINECRAFT_VERSION}.CraftWorld").ruby_class.send :alias_method, :nms_handle, :get_handle
