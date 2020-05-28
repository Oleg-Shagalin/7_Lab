package model;

import oop.model.Service;

import java.util.Collection;
import java.util.List;

public interface Tariff extends Iterable<Service>, Collection<Service> {

    boolean add(int index, Service service);
    Service get(int index);
    Service get(String serviceName);
    Service set(int index, Service service);
    Service remove(int index);
    Service remove(String name);
    Collection<Service> getServices(ServiceTypes type);
    List<Service> sortedServicesByCost();
    double cost();
    int indexOf(Service service);
    int lastIndexOf(Service service);

}
