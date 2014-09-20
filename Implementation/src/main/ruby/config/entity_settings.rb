$entity_config = {
    :Bat => { :sounds => true, :impl => [ RemoteBatImpl, RemoteBatEntity ] },
	:Zombie => { :sounds => true, :impl => [RemoteZombieImpl, RemoteZombieEntity] }
}