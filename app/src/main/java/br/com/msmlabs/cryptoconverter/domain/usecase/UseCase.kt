package br.com.msmlabs.cryptoconverter.domain.usecase

import kotlinx.coroutines.flow.Flow

/**
 * Generic UseCase class
 */
abstract class UseCase<Param, Source> {
    abstract suspend fun execute(param: Param): Flow<Source>

    open suspend operator fun invoke(param: Param) = execute(param)

    abstract class NoParam<Source> : UseCase<None, Flow<Source>>() {
        abstract suspend fun execute(): Flow<Source>

        final override suspend fun execute(param: None) =
            throw UnsupportedOperationException()

        suspend operator fun invoke(): Flow<Source> = execute()
    }

    abstract class NoSource<Params> : UseCase<Params, Unit>() {
        override suspend operator fun invoke(param: Params) = execute(param)
    }

    abstract class NoParamOrSource : UseCase<None, None>() {
        abstract override suspend fun execute(param: None): Flow<None>
    }

    object None
}