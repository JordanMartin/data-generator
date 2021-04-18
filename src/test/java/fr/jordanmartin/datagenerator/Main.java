package fr.jordanmartin.datagenerator;

import fr.jordanmartin.datagenerator.provider.base.Constant;
import fr.jordanmartin.datagenerator.provider.object.Expression;
import fr.jordanmartin.datagenerator.provider.object.ObjectProvider;
import fr.jordanmartin.datagenerator.provider.random.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        ObjectProvider subProvider = new ObjectProvider()
                .field("a", new RandomInt(0, 100))
                .field("b", new Constant<>("test"));

        ObjectProvider rootProvider = new ObjectProvider()
                .providerRef("codeCp", new RandomFromRegex("P[A-Z]{3}[0-9]{5}", 1))
                .field("_id", new RandomUUID())
                .field("dateDebutAbsence", new RandomDate().from(2020, 12, 25).to(2021, 12, 25))
                .field("dateFinAbsence", new RandomDate().from(2020, 12, 25).to(2021, 12, 25))
                .field("etatAbsence", new RandomInt(0, 1000))
                .field("uniteUtil", new Constant<>("11"))
                .field("dureeUtil", new Constant<>("2"))
                .field("millesime", new Constant<>("2021"))
                .field("dateAcquis", new RandomDate().from(2020, 12, 25).to(2021, 12, 25))
                .field("dateEtat", new RandomDate().from(2020, 12, 25).to(2021, 12, 25))
                .field("dateDemandeAbsence", new RandomDate().from(2020, 12, 25).to(2021, 12, 25))
                .field("source", new Constant<>("1"))
                .field("complementSource", new Constant<>("ACH1"))
                .field("urlAbsReferentiel", new Constant<>("urlAbsReferentiel"))
                .field("libelleAbsReferentiel", new Constant<>("urlAbsReferentiel"))
                .field("idAgentReferentiel", new Expression("${codeCp}"))
                .field("urlAgentReferentiel", new Constant<>("urlAbsReferentiel"))
                .field("codeCPAgent", new Expression("ID-${codeCp}"))
                .field("familleMetier", new RandomFromList<>("A", "B", "C"))
                .field("child", subProvider);
    }
}
