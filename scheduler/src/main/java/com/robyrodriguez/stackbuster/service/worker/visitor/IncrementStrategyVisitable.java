package com.robyrodriguez.stackbuster.service.worker.visitor;

public interface IncrementStrategyVisitable {
    void accept(IncrementStrategyVisitor visitor) throws Exception;
}
