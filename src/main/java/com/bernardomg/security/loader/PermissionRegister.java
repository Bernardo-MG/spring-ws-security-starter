package com.bernardomg.security.loader;


public interface PermissionRegister {
    
    public Iterable<String> getActions();
    
    public Iterable<String> getResources();
    
    public Iterable<String> getPermissions();

}
