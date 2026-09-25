package com.xmajer.importapp.importer.cli;

import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Component
public class ImportOptionsParser {

    public ImportOptions parse(ApplicationArguments arguments) {
        var command = new ImportCommand();
        new CommandLine(command).parseArgs(arguments.getSourceArgs());

        return new ImportOptions(command.municipalityExtended);
    }

    @Command(name = "importer")
    private static class ImportCommand {

        @Option(
                names = "--municipality-extended",
                description = "Import extended municipality fields."
        )
        private boolean municipalityExtended;
    }
}
