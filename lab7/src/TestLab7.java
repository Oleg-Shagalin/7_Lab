import model.*;
import oop.model.*;

import java.time.LocalDate;
import java.util.*;

public class TestLab7 {

    public static void lab7tests() {
        Tariff individualsTariff = new IndividualsTariff();
        individualsTariff.add(new Service());
        individualsTariff.set(3, new Service("asd", 123, ServiceTypes.MAIL, LocalDate.now().minusDays(12)));
        individualsTariff.add(new Service("asd", 173, ServiceTypes.MAIL, LocalDate.now()));
        List<Service> list = individualsTariff.sortedServicesByCost();
        Collection<Service> collection = individualsTariff.getServices(ServiceTypes.MAIL);

        Tariff entityTariff = new EntityTariff();
        entityTariff.add(new Service());
        entityTariff.add(new Service("asd", 123, ServiceTypes.MAIL, LocalDate.now().minusDays(13)));
        list = entityTariff.sortedServicesByCost();
        collection = entityTariff.getServices(ServiceTypes.MAIL);

        Account account = new IndividualAccount(0xE8D4A51001L,
                new Person("Ivan", "Ivanov"), (IndividualsTariff) individualsTariff, LocalDate.now());
        Account entityAccount = new EntityAccount(0x738D7EA4C67FFFL,"Petr", entityTariff, LocalDate.now());
        Account[] accounts = new Account[3];
        accounts[0] = account;
        accounts[1] = entityAccount;
        AccountsManager manager = new AccountsManager(accounts);
        Set<Account> set = manager.getAccounts(ServiceTypes.INTERNET);
        List<Account> accountList = manager.getIndividualAccount();
        accountList = manager.getEntityAccount();
    }

    @org.junit.Test
    public void startTests() {
        lab7tests();
    }
}
