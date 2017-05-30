package filters;

import ninja.Filter;
import ninja.FilterChain;
import ninja.Context;
import ninja.Result;

/**
 * Second Filter
 *
 */
public class SecondFilter implements Filter {

    @Override
    public Result filter(FilterChain chain, Context context) {

        System.out.println("SecondBefore");
        Result next = chain.next(context);
        System.out.println("SecondAfter");
        return next;

    }
}