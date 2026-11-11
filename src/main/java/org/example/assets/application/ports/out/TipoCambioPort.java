package org.example.assets.application.ports.out;

import java.math.BigDecimal;

public interface TipoCambioPort {

    BigDecimal convertir(BigDecimal monto, String monedaOrigen, String monedaDestino);
}

