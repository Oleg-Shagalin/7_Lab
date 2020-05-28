package oop.model;

import model.ServiceTypes;
import model.Tariff;

import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.function.Consumer;

public class IndividualsTariff implements Tariff, Cloneable {

    @MagicConstant(intValues = {SERVICE_CHARGE})
    private Service[] services;
    private int size = 0;
    public static final int SERVICE_CHARGE = 50;

    public IndividualsTariff() {
        services = new Service[8];
    }

    public IndividualsTariff(int size) {
        services = new Service[size];
    }

    public IndividualsTariff(Service[] services) {
        this.services = services;

        for (Service service : services) {
            if (service != null)
                size++;
        }

    }

    @Override
    public boolean add(Service service) {
        Objects.requireNonNull(service, "service must not be null");

        for (int i = 0; i < services.length; i++) {
            if (services[i] == null) {
                services[i] = service;
                size++;
                return true;
            }
        }

        increaseArray();
        return add(service);
    }

    private void increaseArray() {
        Service[] temp = new Service[services.length * 2];
        System.arraycopy(services, 0, temp, 0, services.length);
        services = temp;
    }

    @Override
    public boolean add(int index, Service service) {
        Objects.checkIndex(index, services.length);

        if (services[index] == null) {
            services[index] = Objects.requireNonNull(service, "service must not be null");
            size++;
            return true;
        }

        return false;
    }

    @Override
    public Service get(int index) {
        Objects.checkIndex(index, services.length);
        return services[index];
    }

    @Override
    public Service get(String serviceName) {
        Objects.requireNonNull(serviceName, "serviceName must not be null");

        for (Service service : services) {
            if (service != null && service.getName().equals(serviceName))
                return service;
        }

        throw new NoSuchElementException("serviceName not found");
    }

    @Override
    public Service set(int index, Service service) {
        Objects.checkIndex(index, services.length);
        Objects.requireNonNull(service, "service must not be null");

        if (services[index] == null)
            size++;

        services[index] = service;
        return services[index];

    }

    @Override
    public Service remove(int index) {
        Objects.checkIndex(index, services.length);

        Service service = services[index];

        if (index != services.length - 1) {
            System.arraycopy(services, index + 1, services, index, services.length - index - 1);
        }

        if (services[services.length - 1] != null)
            services[services.length - 1] = null;

        if (service != null)
            size--;

        return service;

    }

    @Override
    public Service remove(String serviceName) {
        Objects.requireNonNull(serviceName, "serviceName must not be null");

        for (int i = 0; i < services.length; i++) {
            if (services[i] != null && services[i].getName().equals(serviceName)) {
                return remove(i);
            }
        }

        throw new NoSuchElementException("serviceName not found");
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public LinkedList<Service> getServices(ServiceTypes type) {
        Objects.requireNonNull(type, "type must not be null");

        LinkedList<Service> list = new LinkedList<>();
        Consumer<Service> consumer = new Consumer<Service>() {
            @Override
            public void accept(Service service) {
                if (service != null && service.getType() == type)
                    list.add(service);
            }
        };

        forEach(consumer);
        return list;
    }

    @Override
    public ArrayList<Service> sortedServicesByCost() {
        Service[] temp = (Service[]) toArray();
        Arrays.sort(temp);
        return new ArrayList<>(Arrays.asList(temp));
    }

    @Override
    public double cost() {
        double cost = 0;

        for (Service service : services) {
            if (Objects.nonNull(service)) {
                Period period = Period.between(service.getActivationDate(), LocalDate.now());
                if (period.getMonths() < 1) {
                    cost += service.getCost() * period.getDays() / LocalDate.now().lengthOfMonth();
                }
                else {
                    cost += service.getCost();
                }
            }
        }

        return cost + SERVICE_CHARGE;
    }

    @Override
    public int hashCode() {
        int result = 31;

        for (Service service : services) {
            if (service != null)
                result *= service.hashCode();
        }

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            IndividualsTariff tariff = (IndividualsTariff) obj;
            if (size == tariff.size) {
                Service[] services1 = (Service[]) toArray();
                Service[] services2 = (Service[]) tariff.toArray();
                for (int i = 0; i < size; i++) {
                    if (!services1[i].equals(services2[i]))
                        return false;
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public Tariff clone() throws CloneNotSupportedException {
        IndividualsTariff tariff = (IndividualsTariff) super.clone();

        for (int i = 0; i < services.length; i++) {
            if (services[i] != null)
                tariff.services[i] = services[i].clone();
        }

        return tariff;
    }

    @Override
    public int indexOf(Service service) {
        Objects.requireNonNull(service, "service must not be null");

        for (int i = 0; i < services.length; i++) {
            if (services[i] != null && services[i].equals(service)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int lastIndexOf(Service service) {
        Objects.requireNonNull(service, "service must not be null");

        for (int i = services.length - 1; i >= 0; i--) {
            if (services[i] != null && services[i].equals(service)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (Service service : services) {
            if (service != null)
                builder.append(service.toString());
        }

        return String.format("services:\n%s", builder.toString());
    }

    @Override
    public Iterator<Service> iterator() {
        return new ServiceIterator();
    }

    private class ServiceIterator implements Iterator<Service> {

        private int current_index = 0;

        @Override
        public boolean hasNext() {
            return current_index < services.length;
        }

        @Override
        public Service next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return services[current_index++];
        }

    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        Objects.requireNonNull(o, "o must not be null");

        for (Service service : services) {
            if (service != null && service.equals(o))
                return true;
        }

        return false;
    }

    @NotNull
    @Override
    public Object[] toArray() {
        Service[] temp = new Service[size];
        for (int i = 0, j = 0; i < services.length; i++) {
            if (services[i] != null) {
                temp[j] = services[i];
                j++;
            }
        }

        return temp;
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return (T[]) toArray();
    }

    @Override
    public boolean remove(Object o) {
        Objects.requireNonNull(o, "service must not be null");

        for (int i = 0; i < services.length; i++) {
            if (services[i] != null && services[i].equals(o)) {
                return remove(i) != null;
            }
        }

        return false;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        for (Object o : c) {
            for (Service service : services) {
                if (service != null && !service.equals(o)) {
                    return false;
                }
            }

        }

        return true;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends Service> c) {
        for (Service service : c) {
            if (!add(service))
                return false;
        }

        return true;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        for (Object o : c) {
            if (!remove(o))
                return false;
        }

        return true;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;

        for (Object o : c) {
            for (int i = 0; i < services.length; i++) {
                if (services[i] != null && !services[i].equals(o)) {
                    remove(i);
                    modified = true;
                }
            }

        }

        return modified;
    }

    @Override
    public void clear() {
        for (int i = 0; i < services.length; i++) {
            if (services[i] != null) {
                services[i] = null;
            }
        }
    }

}
