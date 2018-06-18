package com.robyrodriguez.stackbuster.service.worker.visitor;

/**
 * Implemented by working question DOs allowing use of visitor indirection
 */
public interface IncrementStrategyVisitable {
    void accept(IncrementStrategyVisitor visitor) throws Exception;
}
