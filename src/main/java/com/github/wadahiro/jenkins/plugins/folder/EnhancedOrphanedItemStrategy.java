/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2016 Hiroyuki Wada
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.wadahiro.jenkins.plugins.folder;

import java.io.IOException;
import java.util.Collection;

import org.kohsuke.stapler.DataBoundConstructor;

import com.cloudbees.hudson.plugins.folder.computed.ComputedFolder;
import com.cloudbees.hudson.plugins.folder.computed.DefaultOrphanedItemStrategy;
import com.cloudbees.hudson.plugins.folder.computed.OrphanedItemStrategyDescriptor;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import hudson.Extension;
import hudson.model.Project;
import hudson.model.TaskListener;
import hudson.model.TopLevelItem;

/**
 * Enhanced Orphaned Item Strategy class.
 * 
 * @author Hiroyuki Wada
 */
public class EnhancedOrphanedItemStrategy extends DefaultOrphanedItemStrategy {

    private final boolean disableProject;

    @DataBoundConstructor
    public EnhancedOrphanedItemStrategy(boolean pruneDeadBranches, @CheckForNull String daysToKeepStr,
            @CheckForNull String numToKeepStr, boolean disableProject) {
        super(pruneDeadBranches, daysToKeepStr, numToKeepStr);

        this.disableProject = disableProject;
    }

    @SuppressWarnings("unused") // used by Jelly EL
    public boolean isDisableProject() {
        return disableProject;
    }

    @Override
    public <I extends TopLevelItem> Collection<I> orphanedItems(ComputedFolder<I> owner, Collection<I> orphaned,
            TaskListener listener) throws IOException, InterruptedException {
        if (disableProject) {
            for (I item : orphaned) {
                if (item instanceof Project) {
                    Project p = (Project) item;
                    if (!p.isDisabled()) {
                        listener.getLogger().printf("Disable Project: %s%n", item.getFullDisplayName());
                        ((Project) item).disable();
                    }
                }
            }
        }
        return super.orphanedItems(owner, orphaned, listener);
    }

    /**
     * Our {@link hudson.model.Descriptor}
     */
    @Extension
    @SuppressWarnings("unused") // instantiated by Jenkins
    public static class DescriptorImpl extends OrphanedItemStrategyDescriptor {

        /**
         * {@inheritDoc}
         */
        @Override
        public String getDisplayName() {
            return Messages.EnhancedOrphanedItemStrategy_DisplayName();
        }
    }

}