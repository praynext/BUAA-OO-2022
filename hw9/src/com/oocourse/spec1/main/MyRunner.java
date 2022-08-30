package com.oocourse.spec1.main;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import com.oocourse.spec1.exceptions.EqualGroupIdException;
import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.GroupIdNotFoundException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;

public class MyRunner {

    private String[] cmds;
    private Network network;
    private Class<? extends Person> personClass;
    private Class<? extends Network> networkClass;
    private Class<? extends Group> groupClass;
    private Constructor<? extends Person> personConstructor;
    private Constructor<? extends Network> networkConstructor;
    private Constructor<? extends Group> groupConstructor;
    private Scanner cin;

    public MyRunner(Class<? extends Person> personClass, Class<? extends Network> networkClass,
                    Class<? extends Group> groupClass) throws NoSuchMethodException, SecurityException {
        this.personClass = personClass;
        this.networkClass = networkClass;
        this.groupClass = groupClass;
        personConstructor = this.personClass.getConstructor(
                int.class, String.class, int.class);
        networkConstructor = this.networkClass.getConstructor();
        groupConstructor = this.groupClass.getConstructor(int.class);
        cin = new Scanner(System.in);
    }

    public void run()
            throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        this.network = (Network) this.networkConstructor.newInstance();
        while (cin.hasNextLine()) {
            String cmd = cin.nextLine();
            cmds = cmd.split(" ");
            switch (cmds[0]) {
                case "ap":
                    addPerson();
                    break;
                case "ar":
                    addRelation();
                    break;
                case "qv":
                    queryValue();
                    break;
                case "qps":
                    queryPeopleSum();
                    break;
                case "qci":
                    queryCircle();
                    break;
                case "ag":
                    addGroup();
                    break;
                case "atg":
                    addToGroup();
                    break;
                case "dfg":
                    delFromGroup();
                    break;
                case "qbs":
                    queryBlockSum();
                    break;
                case "cn":
                    compareName();
                    break;
                case "qgps":
                    queryGroupPeopleSum();
                    break;
                case "qgvs":
                    queryGroupValueSum();
                    break;
                case "qgam":
                    queryGroupAgeMean();
                    break;
                case "qgav":
                    queryGroupAgeVar();
                    break;
                default:
                    assert (false);
                    break;
            }
        }
        cin.close();
    }

    private void queryBlockSum() {
        System.out.println(network.queryBlockSum());
    }

    private void delFromGroup() {
        int id1 = Integer.parseInt(cmds[1]);
        int id2 = Integer.parseInt(cmds[2]);
        try {
            network.delFromGroup(id1, id2);
        } catch (GroupIdNotFoundException e) {
            e.print();
            return;
        } catch (PersonIdNotFoundException e) {
            e.print();
            return;
        } catch (EqualPersonIdException e) {
            e.print();
            return;
        }
        System.out.println("Ok");
    }

    private void addToGroup() {
        int id1 = Integer.parseInt(cmds[1]);
        int id2 = Integer.parseInt(cmds[2]);
        try {
            network.addToGroup(id1, id2);
        } catch (GroupIdNotFoundException e) {
            e.print();
            return;
        } catch (PersonIdNotFoundException e) {
            e.print();
            return;
        } catch (EqualPersonIdException e) {
            e.print();
            return;
        }
        System.out.println("Ok");
    }

    private void addGroup()
            throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        int id = Integer.parseInt(cmds[1]);
        try {
            network.addGroup((Group) this.groupConstructor.newInstance(id));
        } catch (EqualGroupIdException e) {
            e.print();
            return;
        }
        System.out.println("Ok");
    }

    private void queryCircle() {
        int id1 = Integer.parseInt(cmds[1]);
        int id2 = Integer.parseInt(cmds[2]);
        boolean ret = false;
        try {
            ret = network.isCircle(id1, id2);
        } catch (PersonIdNotFoundException e) {
            e.print();
            return;
        }
        if (ret) {
            System.out.println("1");
        } else {
            System.out.println("0");
        }
    }

    private void queryPeopleSum() {
        int ret = network.queryPeopleSum();
        System.out.println(ret);
    }

    private void queryValue() {
        int id1 = Integer.parseInt(cmds[1]);
        int id2 = Integer.parseInt(cmds[2]);
        int ret = 0;
        try {
            ret = network.queryValue(id1, id2);
        } catch (PersonIdNotFoundException e) {
            e.print();
            return;
        } catch (RelationNotFoundException e) {
            e.print();
            return;
        }
        System.out.println(ret);
    }

    private void addRelation() {
        int id1 = Integer.parseInt(cmds[1]);
        int id2 = Integer.parseInt(cmds[2]);
        int value = Integer.parseInt(cmds[3]);
        try {
            network.addRelation(id1, id2, value);
        } catch (PersonIdNotFoundException e) {
            e.print();
            return;
        } catch (EqualRelationException e) {
            e.print();
            return;
        }
        System.out.println(String.format("Ok"));
    }

    private void addPerson()
            throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        int id = Integer.parseInt(cmds[1]);
        String name = cmds[2];
        int age = Integer.parseInt(cmds[3]);
        try {
            network.addPerson((Person) this.personConstructor.newInstance(
                    id, name, age));
        } catch (EqualPersonIdException e) {
            e.print();
            return;
        }
        System.out.println(String.format("Ok"));
    }

    private void queryGroupAgeVar() {
        int id = Integer.parseInt(cmds[1]);
        int ret = 0;
        if (network.getGroup(id) == null) {
            try {
                network.addToGroup(-1, id);
            } catch (GroupIdNotFoundException e) {
                e.print();
                return;
            } catch (PersonIdNotFoundException | EqualPersonIdException e) {
                return;
            }
        } else {
            ret = network.getGroup(id).getAgeVar();
        }
        System.out.println(ret);
    }

    private void queryGroupAgeMean() {
        int id = Integer.parseInt(cmds[1]);
        int ret = 0;
        if (network.getGroup(id) == null) {
            try {
                network.addToGroup(-1, id);
            } catch (GroupIdNotFoundException e) {
                e.print();
                return;
            } catch (PersonIdNotFoundException | EqualPersonIdException e) {
                return;
            }
        } else {
            ret = network.getGroup(id).getAgeMean();
        }
        System.out.println(ret);
    }

    private void queryGroupValueSum() {
        int id = Integer.parseInt(cmds[1]);
        int ret = 0;
        if (network.getGroup(id) == null) {
            try {
                network.addToGroup(-1, id);
            } catch (GroupIdNotFoundException e) {
                e.print();
                return;
            } catch (PersonIdNotFoundException | EqualPersonIdException e) {
                return;
            }
        } else {
            ret = network.getGroup(id).getValueSum();
        }
        System.out.println(ret);
    }

    private void queryGroupPeopleSum() {
        int id = Integer.parseInt(cmds[1]);
        int ret = 0;
        if (network.getGroup(id) == null) {
            try {
                network.addToGroup(-1, id);
            } catch (GroupIdNotFoundException e) {
                e.print();
                return;
            } catch (PersonIdNotFoundException | EqualPersonIdException e) {
                return;
            }
        } else {
            ret = network.getGroup(id).getSize();
        }
        System.out.println(ret);
    }

    private void compareName() {
        int id1 = Integer.parseInt(cmds[1]);
        int id2 = Integer.parseInt(cmds[2]);
        int ret = 0;
        try {
            network.isCircle(id1, id2);
        } catch (PersonIdNotFoundException e) {
            e.print();
            return;
        }
        Person person1 = network.getPerson(id1);
        Person person2 = network.getPerson(id2);
        ret = person1.compareTo(person2);
        String r;
        if (ret < 0) {
            r = "<";
        } else if (ret == 0) {
            r = "=";
        } else {
            r = ">";
        }
        System.out.println(r);
    }
}