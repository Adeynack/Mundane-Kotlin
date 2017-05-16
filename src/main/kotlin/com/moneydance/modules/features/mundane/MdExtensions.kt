package com.moneydance.modules.features.mundane

import com.infinitekind.moneydance.model.ParentTxn
import com.infinitekind.moneydance.model.SplitTxn
import github.adeynack.kotlin.extensions.tabulate

/**
 * Get an (iterable)[Iterable] list of the (split transactions)[SplitTxn] of this (transaction)[ParentTxn].
 */
val ParentTxn.splits: Iterable<SplitTxn>
    get() = this.splitCount.tabulate(this::getSplit)

