require 'java'
require 'base'

class Object
	def nms_handle;
		self
	end

	def setup_sounds;
	end

	def has_home_area?
		false
	end

	def on_leash?
		false
	end

	def is_in_home_area?(x = 0, y = 0, z = 0)
		true
	end

	def home_range
		5
	end

	def can_be_steered?
		false
	end

	def chunk_coordinates
		if self.is_a?(NMS::EntityCreature)
			self.bT
		else
			NMS::ChunkCoordinates.new NMS::MathHelper.floor(self.locX), NMS::MathHelper.floor(self.locY), NMS::MathHelper.floor(self.locZ)
		end
	end

	def is_remote_entity?
		Java::de.kumpelblase2.remoteentities.RemoteEntities.is_remote_entity self
	end

	def remote_entity
		Java::de.kumpelblase2.remoteentities.RemoteEntities.get_remote_entity_from_entity self if self.is_remote_entity?
	end

	def max_head_rotation
		40
	end

	# For method definitions
	def is_class_def?
		self.is_a? Hash and self.all? { |_, value| value.is_method_def? }
	end

	def is_method_def?
		self.is_a? String or (self.is_a? Hash and self.has_key? :name and self.has_key? :params)
	end
end