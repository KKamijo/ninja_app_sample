package filters;

import ninja.Filter;
import ninja.FilterChain;
import ninja.Context;
import ninja.Result;

/**
 * First Filter
 *
 */
public class FirstFilter implements Filter {

    @Override
    public Result filter(FilterChain chain, Context context) {

        context.setAttribute("foo", "bar");
        System.out.println("FirstBefore");
        Result next = chain.next(context);
        System.out.println("FirstAfter");
        return next;

    }
}