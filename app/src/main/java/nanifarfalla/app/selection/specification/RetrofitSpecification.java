package nanifarfalla.app.selection.specification;

import java.util.HashMap;

/**
 * Representacion de una especificación en formato Retrofit
 */

public interface RetrofitSpecification extends Specification {

    RetrofitGrammar asRetrofitGrammar();

    class RetrofitGrammar {
        HashMap<String, String> headers;
        HashMap<String, String> paths;
        HashMap<String, String> params;
    }
}
