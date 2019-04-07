package io.nuls.ccc.util;

import io.nuls.contract.sdk.Address;
import io.nuls.contract.sdk.Msg;
import io.nuls.contract.sdk.Utils;

public class Owner {

    private Address ownerAddress;

    private static Owner owner;

    private Owner(Address address){
        this.ownerAddress = address;
    }

    public static Owner getOwner(Address sender){
        if(owner == null){
           owner  = new Owner(sender);
        }
        return owner;
    }

    public Address getOwnerAddress(){
        return this.ownerAddress;
    }

    public void requireOwner(String message){
        Utils.require(Msg.sender().equals(ownerAddress),message);
    }
}
