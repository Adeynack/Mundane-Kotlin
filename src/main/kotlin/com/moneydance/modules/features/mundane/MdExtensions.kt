package com.moneydance.modules.features.mundane

import com.infinitekind.moneydance.model.ParentTxn
import com.infinitekind.moneydance.model.SplitTxn

/**
 * Get an (iterable)[Iterable] list of the (split transactions)[SplitTxn] of this (transaction)[ParentTxn].
 */
val ParentTxn.splits: List<SplitTxn>
    get() = (0 until this.splitCount).map(this::getSplit)
